package com.d101.frientree.entity.mongo.leaf;


import com.d101.frientree.entity.leaf.LeafDetail;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "leaf")
public class Leaf {
    @Id
    private String Id;

    private String leafContent;
    private String leafCategory;
    private Long leafComplain;
    private Long leafView;

    public Leaf(LeafDetail leafDetail){
        this.leafContent = leafDetail.getLeafContent();
        this.leafCategory = leafDetail.getLeafCategory();
        this.leafComplain = leafDetail.getLeafComplain();
        this.leafView = leafDetail.getLeafView();
    }

}
