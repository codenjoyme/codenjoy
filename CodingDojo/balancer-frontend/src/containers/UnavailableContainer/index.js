// vendor
import React from 'react';

// own
import Styles from './styles.module.css';

const UnavailableContainer = () => (
    <div className='container'>
        <div className='content'>
            <div className={ Styles.unavailableContainer }>
                <h2 className='title'>Дорогі гравці!</h2>
                <p>
                    На разі ми готуємося до проведення Фіналу між гравцями, які
                    встигли до цього часу пройти відбір і зайняли гідне місце
                    серед фіналістів.
                </p>
                <p>
                    Про те, коли і в якому форматі пройде Фінал, ми
                    проінформуємо фіналістів найближчим часом.{ ' ' }
                </p>
            </div>
        </div>
    </div>
);

export default UnavailableContainer;
