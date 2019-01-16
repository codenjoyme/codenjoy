import React from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import styles from './styles.module.css';
import { book } from '../../routes';

const { checkBox, checkBoxWrap, checkBoxLabel, checkBoxIcon } = styles;

export const CustomCheckboxComponent = ({
    field, // { name, value, onChange, onBlur }
    form: { touched, errors, setTouched }, // also values, setXXXX, handleXXXX, dirty, isValid, status, etc.
    ...props
}) => {
    const isCurrentFieldNotValid = errors[ field.name ];
    const isCurrentFieldValid = touched[ field.name ] && !errors[ field.name ];
    const color = isCurrentFieldNotValid ? '#d35d47' : '#41c7dc';

    return(
        <div className={ checkBoxWrap }>
            <input type={ field.type } id={ field.name } className={ checkBox } { ...field } { ...props } />
            <label htmlFor={ field.name } onClick={ () => setTouched({ ...touched, [ field.name ]: true }) }>
                {
                    isCurrentFieldValid ?
                        <FontAwesomeIcon className={ checkBoxIcon } icon={ [ 'far', 'check-square' ] } style={ {color: '#cedb56'} } /> :
                        <FontAwesomeIcon className={ checkBoxIcon } icon={ [ 'far', 'square' ] } style={ {color: color} } />
                }
            </label>
            <Link to={ book.terms } className={ checkBoxLabel }>{ props.label }</Link>
        </div>
    )
};
