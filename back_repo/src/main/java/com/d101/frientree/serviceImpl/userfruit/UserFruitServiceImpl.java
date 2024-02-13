package com.d101.frientree.serviceImpl.userfruit;

import com.d101.frientree.dto.userfruit.dto.UserFruitSaveDTO;
import com.d101.frientree.dto.userfruit.dto.UserFruitTodayInfoDTO;
import com.d101.frientree.dto.userfruit.request.UserFruitTextRequest;
import com.d101.frientree.dto.userfruit.response.UserFruitCreateResponse;
import com.d101.frientree.dto.userfruit.response.UserFruitSaveResponse;
import com.d101.frientree.dto.userfruit.response.UserFruitTodayInfoResponse;
import com.d101.frientree.entity.fruit.FruitDetail;
import com.d101.frientree.entity.fruit.UserFruit;
import com.d101.frientree.entity.mongo.emotion.Emotion;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.user.UserModifyException;
import com.d101.frientree.exception.userfruit.NaverClovaAPIException;
import com.d101.frientree.exception.userfruit.PythonAPIException;
import com.d101.frientree.exception.fruit.FruitNotFoundException;
import com.d101.frientree.exception.userfruit.UserFruitCreateException;
import com.d101.frientree.exception.userfruit.UserFruitNotFoundException;
import com.d101.frientree.repository.fruit.FruitDetailRepository;
import com.d101.frientree.repository.fruit.UserFruitRepository;
import com.d101.frientree.repository.user.UserRepository;
import com.d101.frientree.repository.mongo.MongoEmotionRepository;
import com.d101.frientree.service.fruit.UserFruitService;
import com.d101.frientree.serviceImpl.userfruit.clova.ClovaSpeechClient;
import com.d101.frientree.serviceImpl.userfruit.clova.ClovaSpeechResponse;
import com.d101.frientree.serviceImpl.userfruit.fastapi.HttpPostAIRequest;
import com.d101.frientree.serviceImpl.userfruit.objectstorage.AwsS3ObjectStorage;
import com.d101.frientree.util.CommonUtil;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
public class UserFruitServiceImpl implements UserFruitService {
    //naver clova speech to text class
    private final ClovaSpeechClient clovaSpeechClient;
    private final HttpPostAIRequest httpPostAIRequest;
    private final AwsS3ObjectStorage awsS3ObjectStorage;
    private final MongoEmotionRepository mongoEmotionRepository;
    private final FruitDetailRepository fruitDetailRepository;
    private final UserFruitRepository userFruitRepository;
    private final UserRepository userRepository;
    private final CommonUtil commonUtil;
    // 생성자를 통한 의존성 주입

    public UserFruitServiceImpl(ClovaSpeechClient clovaSpeechClient, HttpPostAIRequest httpPostAIRequest,
                                AwsS3ObjectStorage awsS3ObjectStorage, MongoEmotionRepository mongoEmotionRepository,
                                FruitDetailRepository fruitDetailRepository, UserFruitRepository userFruitRepository,
                                UserRepository userRepository, CommonUtil commonUtil) {
        this.clovaSpeechClient = clovaSpeechClient;
        this.httpPostAIRequest = httpPostAIRequest;
        this.awsS3ObjectStorage = awsS3ObjectStorage;
        this.mongoEmotionRepository = mongoEmotionRepository;
        this.fruitDetailRepository = fruitDetailRepository;
        this.userFruitRepository = userFruitRepository;
        this.userRepository = userRepository;
        this.commonUtil = commonUtil;
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<ResponseEntity<UserFruitCreateResponse>> speechToTextAudio(MultipartFile file) {
        commonUtil.checkServerInspectionTime();
        User user = commonUtil.getUser();
        Optional<UserFruit> userFruit = userFruitRepository.findByUser_UserIdAndUserFruitCreateDate(
                user.getUserId(),
                LocalDate.now()
        );
        if(userFruit.isPresent()){throw new UserFruitCreateException("Already produced fruit");}
        try{
            // 1. 음성 파일 Aws S3 저장
            String awsS3Path = awsS3ObjectStorage.uploadFile(file);

            // NestRequestEntity 설정 (옵션)
            ClovaSpeechClient.NestRequestEntity requestEntity = new ClovaSpeechClient.NestRequestEntity();
            // 필요한 경우 추가 설정을 여기에 입력
            // 필요한 설정들을 설정
            requestEntity.setLanguage("ko-KR"); // 언어 설정
            requestEntity.setCompletion("sync"); // 완료 유형 설정 (동기/비동기)
            //requestEntity.setCallback("your_callback_url"); // 콜백 URL 설정 (선택 사항)
            // requestEntity.setUserdata(userdataMap); // 사용자 데이터 설정 (선택 사항)
            requestEntity.setWordAlignment(true); // 단어 정렬 설정
            requestEntity.setFullText(true); // 전체 텍스트 인식 결과 출력 설정

            // 발화자 구분(Diarization) 설정 (선택 사항)
            ClovaSpeechClient.Diarization diarization = new ClovaSpeechClient.Diarization();
            diarization.setEnable(false); // 화자 인식 활성화
            //diarization.setSpeakerCountMin(2); // 최소 발화자 수 설정
            //diarization.setSpeakerCountMax(5); // 최대 발화자 수 설정
            requestEntity.setDiarization(diarization);

            // 이벤트 탐지(Sed) 설정 (선택 사항)
            ClovaSpeechClient.Sed sed = new ClovaSpeechClient.Sed();
            sed.setEnable(false); // 이벤트 탐지 활성화
            requestEntity.setSed(sed);

            String result = "";
            try{
                // 2. 파일 업로드 및 인식 요청
                result = clovaSpeechClient.url(awsS3Path, requestEntity);
            }catch (NaverClovaAPIException e){
                throw new NaverClovaAPIException("Naver API Error");
            }
            log.info("AWS S3 URL : {}", awsS3Path);
            // 3. 파일 삭제
            awsS3ObjectStorage.deleteFile(awsS3Path);

            // Gson을 사용하여 응답을 ClovaSpeechResponse 객체로 파싱
            Gson gson = new Gson();
            ClovaSpeechResponse response = gson.fromJson(result, ClovaSpeechResponse.class);

            // 전체 텍스트 추출
            String fullText = response.getFullText();

            log.info("음성 Text : {}", response.getFullText());
            try {//Python 감정 분석 API 호출
                ResponseEntity<UserFruitCreateResponse> sttResponse =
                        ResponseEntity.ok(httpPostAIRequest.sendPostRequest(fullText));
                return CompletableFuture.completedFuture(sttResponse);
            }catch (PythonAPIException e){
                throw new PythonAPIException("Python AI API Error");
            }
        }catch (Exception e){
            // 예외 처리
            CompletableFuture<ResponseEntity<UserFruitCreateResponse>> completableFuture = new CompletableFuture<>();
            completableFuture.completeExceptionally(e);
            return completableFuture;
        }
    }
    @Override
    public ResponseEntity<UserFruitCreateResponse> speechToTextText(UserFruitTextRequest textFile) throws Exception {
        commonUtil.checkServerInspectionTime();
        User user = commonUtil.getUser();
        Optional<UserFruit> userFruit = userFruitRepository.findByUser_UserIdAndUserFruitCreateDate(
                user.getUserId(),
                LocalDate.now()
        );
        if(userFruit.isPresent()){throw new UserFruitCreateException("Already produced fruit");}
        try { //Python 감정 분석 API 호출
            return ResponseEntity.ok(httpPostAIRequest.sendPostRequest(textFile.getContent()));
        }catch (PythonAPIException e){
            throw new PythonAPIException("Python AI API Error");
        }
    }

    @Transactional
    @Override
    public ResponseEntity<UserFruitSaveResponse> userFruitSave(Long fruitNum) {
        commonUtil.checkServerInspectionTime();
        //사용자 정보 가져오기 (PK 값)
        User user = commonUtil.getUser();
        Optional<UserFruit> userFruitOptional =
        userFruitRepository.findByUser_UserIdAndUserFruitCreateDate(
                user.getUserId(), LocalDate.now());
        if(userFruitOptional.isPresent()){
            throw new UserFruitCreateException("Already produced fruit");
        }

        //반환 객체 미리 생성
        UserFruitSaveResponse userFruitSaveResponse;

        //Apple 생성 여부 확인
        boolean isApple = false;

        //열매 디테일 들고오기
        Optional<FruitDetail> optionalFruitDetail = fruitDetailRepository.findById(fruitNum);
        FruitDetail fruitDetail;
        if(optionalFruitDetail.isPresent()){
            fruitDetail = optionalFruitDetail.get();
        }else{ //열매 디테일 정보 없음
            throw new FruitNotFoundException("Fruit Not Found");
        }

        //수정된 결과 NoSQL 수정(유저 PK 값 기준으로 가장 최근 저장된 감정 데이터 수정하기)
        Optional<Emotion> emotionOptional = mongoEmotionRepository.findTopByUserPKOrderByTimestampDesc(String.valueOf(user.getUserId()));
        if(emotionOptional.isPresent()){
            Emotion emotion = emotionOptional.get();
            //저장된 감정 결과와 사용자 최종 감정과 다를 경우 수정
            if(!emotion.getEmotionResult().equals(fruitDetail.getFruitFeel())){
                emotion.setEmotionResult(fruitDetail.getFruitFeel());
                emotion.setTimestamp(Instant.now());
                mongoEmotionRepository.save(emotion);
            }
        }

        //점수 구간에 따른 랜덤 점수 정해주기
        long minScore = fruitDetail.getFruitMinScore();
        long maxScore = fruitDetail.getFruitMaxScore();

        long userScore = minScore + (long) (Math.random() * (maxScore - minScore + 1));

        //딸기일 때 -- 현재 시간 밀리세컨드 단위로 뒤에 숫자 한 개 들고와서 랜덤 점수 끝자리랑 같은지 비교
        //같으면 luck 열매 사과 생성해주기
        if(fruitDetail.getFruitFeel().equals("happy")){
            long currentTimeMillis = System.currentTimeMillis();

            if((userScore % 10) == (currentTimeMillis % 10)){
                userScore = 22;
                //luck 열매로 정보 바꾸기
                optionalFruitDetail = fruitDetailRepository.findByFruitFeel("luck");
                fruitDetail = optionalFruitDetail.get();
                isApple = true;
            }
        }

        //UserFruit Table 생성날짜, 유저 열매 점수, 유저 PK, 생성된 열매 번호 insert
        UserFruit newUserFruit = new UserFruit();
        Optional<User> optionalUser = userRepository.findById(user.getUserId());
        if(optionalUser.isPresent()) {
            try{ //유저 열매 생성 상태 변경
                int isChange = userRepository.updateUserFruitStatusById(optionalUser.get().getUserId(), false);
                if(isChange>0){ //수정 성공
                    newUserFruit = UserFruit.createUserFruit(optionalUser.get(), fruitDetail, LocalDate.now(), userScore);
                }
            }catch (UserModifyException e){ //수정 실패
                throw new UserModifyException("User Modify Exception");
            }
        }
        //UserFruit Table 정보 저장
        userFruitRepository.save(newUserFruit);

        //UserFruitSaveResponse 생성
        userFruitSaveResponse = UserFruitSaveResponse.createUserFruitSaveResponse("Success"
                , UserFruitSaveDTO.createUserFruitSaveDTO(
                        isApple,
                        newUserFruit.getUserFruitCreateDate().toString(),
                        fruitDetail,
                        userScore)
        );

        //UserFruitSaveResponse 반환
        return ResponseEntity.ok(userFruitSaveResponse);
    }

    @Override
    public ResponseEntity<UserFruitTodayInfoResponse> userFruitTodayInfo(String createDate) {
        commonUtil.checkServerInspectionTime();
        User user = commonUtil.getUser();

        Optional<UserFruit> userFruit = userFruitRepository.findByUser_UserIdAndUserFruitCreateDate(
                user.getUserId(), LocalDate.now());
        if(userFruit.isEmpty()){throw new UserFruitNotFoundException("User Fruit Not Found");}

        UserFruitTodayInfoResponse response =
                UserFruitTodayInfoResponse.createUserFruitTodayInfoResponse(
                "Success",
                UserFruitTodayInfoDTO.createUserFruitTodayInfoDTO(userFruit.get())
        );

        return ResponseEntity.ok(response);
    }
}
