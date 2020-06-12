import React from 'react';

import { CustomInputComponent } from './customInput';

import styles from './styles.module.css';

const { phoneInputWrap, phonePrefix } = styles;

export const PhoneInput = (props) => {
  return (
    <CustomInputComponent className={ phoneInputWrap } { ...props }>
      <div className={ phonePrefix }> +38 </div>
    </CustomInputComponent>
  )
}
