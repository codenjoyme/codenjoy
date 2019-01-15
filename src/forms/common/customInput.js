import React from 'react';
import classNames from 'classnames';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import styles from './styles.module.css';

const { input, inputError, inputValid, inputWrap, inputIcon } = styles;

export const CustomInputComponent = ({
    field, // { name, value, onChange, onBlur }
    form: { touched, errors }, // also values, setXXXX, handleXXXX, dirty, isValid, status, etc.
    ...props
}) => {
    const isCurrentFieldNotValid = touched[ field.name ] && errors[ field.name ];
    const isCurrentFieldValid = touched[ field.name ] && !errors[ field.name ];

    return(
        <div className={ inputWrap }>
            <input type='text' className={ classNames(input, { [ inputError ]: isCurrentFieldNotValid, [ inputValid ]: isCurrentFieldValid  }) } { ...field } { ...props } />
            { isCurrentFieldNotValid && <FontAwesomeIcon className={ inputIcon } icon='times-circle' style={ {color: '#d35d47'} } /> }
            { isCurrentFieldValid && <FontAwesomeIcon className={ inputIcon } icon='check-circle' style={ {color: '#cedb56'} } /> }
        </div>
    )
};
