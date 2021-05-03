package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.UserPortrait;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserPortraitDAO
{
    @Select("select * from user_portrait where uid=#{uid}")
    UserPortrait getUserPortrait(int uid);

    @Insert("INSERT INTO user_portrait (uid, count_GoodComment, count_MediumComment, count_BadComment, count_punishedScore, count_additionalScore, count_send_GoodsComment, count_send_MediumComment, count_send_BadComment) VALUES (#{uid},0,0,0,0,0,0,0,0)")
    void newUser(UserPortrait userPortrait);

    @Update("update user_portrait set count_additionalScore=#{count_additionalScore},count_punishedScore=#{count_punishedScore},count_GoodComment=#{count_GoodComment},count_MediumComment=#{count_MediumComment},count_BadComment=#{count_BadComment}" +
            ",count_send_BadComment=#{count_send_BadComment},count_send_GoodsComment=#{count_send_GoodsComment},count_send_MediumComment=#{count_send_MediumComment}")
    void update(UserPortrait userPortrait);

    @Update("update user_portrait set count_additionalScore =#{count_additionalScore} where uid=#{uid}")
    void updateAdditionalScore(@Param("uid") int uid, @Param("count_additionalScore") int count_additionalScore);

    @Update("update user_portrait set count_punishedScore=#{count_punishedScore} where uid=#{uid}")
    void updatePunishedScore(@Param("uid") int uid, @Param("count_punishedScore") int count_punishedScore);

    @Select("select count_additionalScore from user_portrait where uid=#{uid}")
    int getAdditionalScore(int uid);

    @Select("select count_punishedScore from user_portrait where uid=#{uid}")
    int getPunishedScore(int uid);
}


