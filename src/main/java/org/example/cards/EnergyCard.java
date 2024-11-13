package org.example.cards;

import org.example.players.Player;

public class EnergyCard implements Card {
    private int energyRecovery;

    public EnergyCard(int energyRecovery) {
        this.energyRecovery = energyRecovery;
    }

    @Override
    public void execute(Player user, Player opponent) {
        user.setEnergy(user.getEnergy() + energyRecovery);
        System.out.println(user.getName() + " recovers " + energyRecovery + " energy");
    }

    @Override
    public String getName() {
        return "Energy Recovery";
    }
}
