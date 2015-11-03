package com.codenjoy.dojo.quake2d.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Артефакт Золото на поле
 */
public class Ability extends PointImpl implements State<Elements, Player> {
    public static final int HEALTH_BONUS = 30;
    enum Type {WEAPON, DEFENCE, HEALTH}

    private Type abilityType;

    public Ability(int x, int y, Dice dice) {
        super(x, y);
        int randomChoice = dice.next(Type.values().length);
        for (Type elem : Type.values()){
            if (elem.ordinal() == randomChoice){
                abilityType = elem;
            }
        }
    }

    public Ability(Point point, Type abilityType) {
        super(point);
        this.abilityType = abilityType;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (abilityType == Type.WEAPON){
            return Elements.SUPER_ATTACK;
        } else if (abilityType == Type.DEFENCE) {
            return Elements.SUPER_DEFENCE;
        } else {
            return Elements.HEALTH_PACKAGE;
        }
    }

    public Type getAbilityType() {
        return abilityType;
    }
}
