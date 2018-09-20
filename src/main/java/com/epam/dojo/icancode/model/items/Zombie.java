package com.epam.dojo.icancode.model.items;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Tickable;
import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.Hero;
import com.epam.dojo.icancode.model.interfaces.IItem;

public class Zombie extends FieldItem implements Tickable {

    public static ZombieBrain BRAIN = new ZombieBrain();

    public Zombie(boolean gender) {
        super(getElement(gender));
    }

    private static Elements getElement(boolean gender) {
        return (gender) ? Elements.MALE_ZOMBIE : Elements.FEMALE_ZOMBIE;
    }

    @Override
    public void action(IItem item) {
        HeroItem heroItem = get(item, HeroItem.class);
        if (heroItem == null) {
            return;
        }

        Hero hero = heroItem.getHero();
        if (!hero.isFlying()) {
            hero.dieOnZombie();
        }
    }

    @Override
    public void tick() {
        Direction direction = BRAIN.whereToGo(field);
        int newX = direction.changeX(getCell().getX());
        int newY = direction.changeY(getCell().getY());

        if (!field.isBarrier(newX, newY)) {
            field.move(this, newX, newY);
        }
    }
}
