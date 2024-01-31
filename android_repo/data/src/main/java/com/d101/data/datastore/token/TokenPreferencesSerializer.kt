package com.d101.data.datastore.token

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.d101.data.datastore.TokenPreferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object TokenPreferencesSerializer : Serializer<TokenPreferences> {
    override val defaultValue: TokenPreferences = TokenPreferences.newBuilder()
        .setAccessToken("NEED_LOGIN")
        .setRefreshToken("NEED_LOGIN")
        .build()

    override suspend fun readFrom(input: InputStream): TokenPreferences {
        try {
            return TokenPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: TokenPreferences, output: OutputStream) = t.writeTo(output)
}
