package com.d101.data.datastore.user

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.d101.data.datastore.UserStatusPreferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserStatusPreferencesSerializer : Serializer<UserStatusPreferences> {

    override val defaultValue: UserStatusPreferences = UserStatusPreferences.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): UserStatusPreferences {
        try {
            return UserStatusPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserStatusPreferences, output: OutputStream) = t.writeTo(output)
}
