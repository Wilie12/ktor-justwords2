package example.com.user

import example.com.data.user.db.entity.UserInfo
import org.bson.types.ObjectId

fun UserInfo.toUserInfoSerializable(): UserInfoSerializable {
    return UserInfoSerializable(
        dailyStreak = dailyStreak,
        bestStreak = bestStreak,
        currentGoal = currentGoal,
        dailyGoal = dailyGoal,
        lastPlayedTimestamp = lastPlayedTimestamp,
        userName = userName,
        userId = userId.toHexString()
    )
}

fun UserInfoSerializable.toUserInfo(): UserInfo {
    return UserInfo(
        dailyStreak = dailyStreak,
        bestStreak = bestStreak,
        currentGoal = currentGoal,
        dailyGoal = dailyGoal,
        lastPlayedTimestamp = lastPlayedTimestamp,
        userName = userName,
        userId = ObjectId(userId)
    )
}