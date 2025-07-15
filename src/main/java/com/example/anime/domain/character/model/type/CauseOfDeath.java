package com.example.anime.domain.character.model.type;

import com.example.anime.domain.character.exception.CharacterCauseOfDeathCastingException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum CauseOfDeath {
    NaturalDeath("자연사(自然死)"), // 자연사
    DeathByDisease("병사(病死)"), // 병사
    Suicide("자살(自殺)"), // 자살
    Unknown("불명사(不明死)"), // 불명사
    Homicide("타살(他殺)"), // 타살
    SuddenDeath("돌연사(突然死)") // 돌연사
    ;
    private final String deathReason;
    private final static Map<String, CauseOfDeath> deathReasonMap = Arrays.stream(values())
                    .collect(Collectors.toMap(CauseOfDeath::getDeathReason, Function.identity()));

    public static CauseOfDeath valueOfDeathReason(String deathReason) {
        return Optional.ofNullable(deathReasonMap.get(deathReason))
                .orElseThrow(CharacterCauseOfDeathCastingException::getInstance);
    }
}
