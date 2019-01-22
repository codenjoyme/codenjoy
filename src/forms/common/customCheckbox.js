import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import styles from './styles.module.css';

const {
    checkBox,
    checkBoxWrap,
    checkBoxLabel,
    checkBoxItem,
    checkBoxIcon,
} = styles;

export const CustomCheckboxComponent = ({
    field, // { name, value, onChange, onBlur }
    form: { touched, errors, setTouched }, // also values, setXXXX, handleXXXX, dirty, isValid, status, etc.
    ...props
}) => {
    const isCurrentFieldNotValid = errors[ field.name ];
    const isCurrentFieldValid = touched[ field.name ] && !errors[ field.name ];
    const color = isCurrentFieldNotValid ? '#d35d47' : '#41c7dc';

    return (
        <div className={ checkBoxWrap }>
            <input
                type={ field.type }
                id={ field.name }
                className={ checkBox }
                { ...field }
                { ...props }
            />
            <label
                className={ checkBoxItem }
                htmlFor={ field.name }
                onClick={ () => setTouched({ ...touched, [ field.name ]: true }) }
            >
                { isCurrentFieldValid ? (
                    <FontAwesomeIcon
                        className={ checkBoxIcon }
                        icon={ [ 'far', 'check-square' ] }
                        style={ { color: '#cedb56' } }
                    />
                ) : (
                    <FontAwesomeIcon
                        className={ checkBoxIcon }
                        icon={ [ 'far', 'square' ] }
                        style={ { color: color } }
                    />
                ) }
            </label>
            <div className={ checkBoxLabel }>{ props.label }</div>
        </div>
    );
};
