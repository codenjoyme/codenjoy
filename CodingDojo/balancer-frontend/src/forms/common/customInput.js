import React from 'react';
import classNames from 'classnames';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import styles from './styles.module.css';

const { input, inputError, inputValid, inputWrap, inputIcon } = styles;

export const CustomInputComponent = ({
  field, // { name, value, onChange, onBlur }
  form: { touched, errors: validationErrors }, // also values, setXXXX, handleXXXX, dirty, isValid, status, etc.
  errors,
  children,
  className,
  ...props
}) => {
  const isCurrentFieldNotValid = touched[field.name] && (validationErrors[field.name] || errors);
  const isCurrentFieldValid = touched[field.name] && !validationErrors[field.name] && !errors;
  return (
    <div className={ classNames(inputWrap, className) }>
      { children }
      <input
        type={ field.type }
        className={ classNames(input, { [inputError]: isCurrentFieldNotValid, [inputValid]: isCurrentFieldValid }) }
        { ...field }
        { ...props }
      />
      { isCurrentFieldNotValid && <FontAwesomeIcon className={ inputIcon } icon='times-circle' style={ { color: '#d35d47' } } /> }
      { isCurrentFieldValid && <FontAwesomeIcon className={ inputIcon } icon='check-circle' style={ { color: '#cedb56' } } /> }
    </div>
  )
};
