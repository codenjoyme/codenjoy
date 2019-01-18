// vendor
import React, { PureComponent } from 'react';
import { NavLink } from 'react-router-dom';
import classnames from 'classnames';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// proj
import { book } from '../../routes';
import Styles from './styles.module.css';

export class Footer extends PureComponent {
    render() {
        return (
            <div className={ Styles.footer }>
                <ul className={ Styles.navigation }>
                    <li>
                        <NavLink to={ book.rules }>
                            Правила і положення
                        </NavLink>
                    </li>
                    <li>
                        <NavLink to={ book.board }>
                            Політика конфіденцій
                        </NavLink>
                    </li>
                </ul>
                <ul className={ classnames(Styles.navigation, Styles.navigation) }>
                    <li>
                        <a href='mailto:example@tutorialspark.com'>
                            Зв'язатися з нами
                            <FontAwesomeIcon className={ Styles.navigationIcon } icon={ [ 'far', 'envelope' ] } style={ {color: '#fff'} } />
                        </a>
                    </li>
                    <li>
                        <a href='/'>
                            Поділитися
                            <FontAwesomeIcon className={ Styles.navigationIcon } icon={ [ 'fas', 'share-alt' ] } style={ {color: '#fff'} } />
                        </a>
                    </li>
                    <li>
                        <a href='/'>
                            Долучайся до чату
                            <FontAwesomeIcon className={ Styles.navigationIcon } icon={ [ 'far', 'comments' ] } style={ {color: '#fff'} } />
                        </a>
                    </li>
                </ul>
            </div>
        );
    }
}
