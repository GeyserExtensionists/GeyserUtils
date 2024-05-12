package me.zimzaza4.geyserutils.geyser.scoreboard;

import org.cloudburstmc.protocol.bedrock.data.ScoreInfo;
import org.cloudburstmc.protocol.bedrock.packet.SetScorePacket;
import org.geysermc.geyser.scoreboard.Scoreboard;
import org.geysermc.geyser.session.GeyserSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityScoreboard {
    private final GeyserSession session;

    private final Map<String, Map<Long, ScoreInfo>> objectives = new ConcurrentHashMap();
    private Scoreboard scoreboard;

    public EntityScoreboard(GeyserSession session) {
        this.session = session;
        this.scoreboard = session.getWorldCache().getScoreboard();
    }


    public void updateScore(String objective, long entityId, int score) {
        Scoreboard scoreboard = session.getWorldCache().getScoreboard();
        Map<Long, ScoreInfo> scores = objectives.computeIfAbsent(objective, k -> new HashMap<>());
        ScoreInfo info = scores.get(entityId);
        if (info == null) {
            info = new ScoreInfo(scoreboard.getNextId().getAndIncrement(), objective, score, ScoreInfo.ScorerType.ENTITY, entityId);
        }
        List<ScoreInfo> infos = new ArrayList<>();
        infos.add(info);
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.setAction(SetScorePacket.Action.SET);
        setScorePacket.setInfos(infos);
        this.session.sendUpstreamPacket(setScorePacket);

    }

}
