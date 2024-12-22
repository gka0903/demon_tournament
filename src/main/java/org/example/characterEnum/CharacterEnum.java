package org.example.characterEnum;

import org.example.character.Character;

import java.awt.Point;

public enum CharacterEnum {
    INUYASHA(
            "Inuyasha",
            "src/main/resources/animations/cards/이누야샤/이누야샤기본모션200x160.gif",
            "src/main/resources/animations/cards/이누야샤/이누야샤 점프200x160.gif",
            "src/main/resources/animations/cards/이누야샤/이누야샤힐200x160.gif",
            "src/main/resources/animations/cards/이누야샤/이누야샤방어200x160.gif",
            "src/main/resources/animations/cards/이누야샤/이누야샤추가공격200x160.gif",
            "src/main/resources/animations/cards/이누야샤/이누야샤기본공격200x160.gif",
            "src/main/resources/animations/cards/이누야샤/이누야샤맞는모션200x160.gif",
            new Point(1, 1)
    ),
    SESSHOMARU(
            "Sesshomaru",
            "src/main/resources/animations/cards/셋쇼마루/셋쇼제자리.gif",
            "src/main/resources/animations/cards/셋쇼마루/셋쇼점프.gif",
            "src/main/resources/animations/cards/셋쇼마루/셋쇼힐.gif",
            "src/main/resources/animations/cards/셋쇼마루/셋쇼방어.gif",
            "src/main/resources/animations/cards/셋쇼마루/셋쇼추가공격.gif",
            "src/main/resources/animations/cards/셋쇼마루/셋쇼공격.gif",
            "src/main/resources/animations/cards/셋쇼마루/셋쇼맞음.gif",
            new Point(3, 1)
    );

    private final String name;
    private final String idleImage;
    private final String moveImage;
    private final String healImage;
    private final String defenseImage;
    private final String attack1Image;
    private final String attack2Image;
    private final String hitImage;
    private final Point initialPosition;

    CharacterEnum(String name, String idleImage, String moveImage, String healImage, String defenseImage,
                  String attack1Image, String attack2Image, String hitImage, Point initialPosition) {
        this.name = name;
        this.idleImage = idleImage;
        this.moveImage = moveImage;
        this.healImage = healImage;
        this.defenseImage = defenseImage;
        this.attack1Image = attack1Image;
        this.attack2Image = attack2Image;
        this.hitImage = hitImage;
        this.initialPosition = initialPosition;
    }

    public Character createCharacter() {
        return new Character(name, idleImage, moveImage, healImage, defenseImage, attack1Image, attack2Image, hitImage, initialPosition);
    }

    public static Character getCharacterByName(String name) {
        for (CharacterEnum characterEnum : values()) {
            if (characterEnum.name.equalsIgnoreCase(name)) {
                return characterEnum.createCharacter();
            }
        }
        throw new IllegalArgumentException("No character found with name: " + name);
    }
}
