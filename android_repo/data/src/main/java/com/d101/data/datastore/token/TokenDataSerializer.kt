package com.d101.data.datastore.token

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.d101.data.datastore.TokenData
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object TokenDataSerializer : Serializer<TokenData> {
    override val defaultValue: TokenData = TokenData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TokenData {
        try {
            return TokenData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: TokenData, output: OutputStream) = t.writeTo(output)
}

val Context.settingDataStore: DataStore<TokenData> by dataStore(
    fileName = "token_data.pb",
    serializer = TokenDataSerializer,
)
