package io.github.azusalad.isosta.data

import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.model.Thumbnail
import kotlinx.coroutines.flow.Flow

interface UserRoomRepository {

    fun loadAllUserStream(): Flow<List<IsostaUser>>

    fun loadAllLinkStream(): Flow<List<String>>

    fun loadUserStream(userHandle: String): Flow<IsostaUser?>

    suspend fun insertUser(user: IsostaUser)

    suspend fun deleteUser(userHandle: String)

    suspend fun updateUser(user: IsostaUser)

}
