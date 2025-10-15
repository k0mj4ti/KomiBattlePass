package me.sheephun.komiBattlePass.data;

import me.sheephun.komiBattlePass.enums.RewardType;

public class Reward {
    private RewardType rewardType;
    private Object data;

    public Reward(RewardType rewardType, Object data){
        this.rewardType = rewardType;
        this.data = data;
    }

    public RewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

