package io.github.azusalad.isosta.data

import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.model.Thumbnail
import kotlinx.coroutines.flow.Flow

interface UserRoomRepository {

    fun loadAllUserStream(): Flow<List<IsostaUser>>

    fun loadUserStream(userHandle: String): Flow<IsostaUser?>

    suspend fun insertUser(user: IsostaUser)

    suspend fun deleteUser(user: IsostaUser)

    suspend fun updateUser(user: IsostaUser)

}
