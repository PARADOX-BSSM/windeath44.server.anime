package com.example.anime.domain.character.model.type;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum CauseOfDeath {
    NaturalDeath("자연사"), // 자연사
    DeathByDisease("병사"), // 병사
    Suicide("자살"), // 자살
    Unknown("불명사"), // 불명사
    Homicide("타살"), // 타살
    SuddenDeath("돌연사") // 돌연사
    ;
    String deathReason;
    private final static Map<String, CauseOfDeath> deathReasonMap = Arrays.stream(values())
                    .collect(Collectors.toMap(CauseOfDeath::getDeathReason, Function.identity()));

    public static CauseOfDeath valueOfDeathReason(String deathReason) {
        return deathReasonMap.get(deathReason);
    }
}
