package org.example.test;

import org.example.character.Character;
import org.example.card.Card;

import java.util.stream.Collectors;

public class CharacterConverter {
    public static SerializableCharacter toSerializable(Character character) {
        return new SerializableCharacter(
                character.getName(),
                character.getIdleImage().toString(), // 경로만 추출
                character.getMoveImage().toString(),
                character.getHealImage().toString(),
                character.getDefenseImage().toString(),
                character.getAttack1Image().toString(),
                character.getAttack2Image().toString(),
                character.getGridPosition(),
                character.getHealth(),
                character.getStamina(),
                character.getCardList().stream()
                        .map(card -> new SerializableCardData(
                                card.getCardType(),
                                card.getCardData().getDamage(),
                                card.getCardData().getStamina(),
                                card.getCardData().getCardSpritePath(),
                                card.getCardData().getCardBackSpritePath(),
                                card.getCardData().getMoveDir(),
                                card.getCardData().getAtkShape()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
