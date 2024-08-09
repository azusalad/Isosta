package io.github.azusalad.isosta.data

import io.github.azusalad.isosta.model.IsostaUser
import kotlinx.coroutines.flow.Flow

class OfflineUserRoomRepository(private val userDao: UserDao) : UserRoomRepository {
    override fun loadAllUserStream(): Flow<List<IsostaUser>> = userDao.loadAllUser()

    override fun loadUserStream(userHandle: String): Flow<IsostaUser?> = userDao.loadUser(userHandle)

    override suspend fun insertUser(user: IsostaUser) = userDao.insert(user)

    override suspend fun deleteUser(user: IsostaUser) = userDao.delete(user)

    override suspend fun updateUser(user: IsostaUser) = userDao.update(user)
}
