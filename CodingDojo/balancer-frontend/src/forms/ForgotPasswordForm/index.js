// core
import React, { Component } from 'react';
import { Formik, Form, Field } from 'formik';
import { connect } from 'react-redux';
import * as Yup from 'yup';
import _ from 'lodash';

// proj
import { resetPasswordStart, resetPasswordValidateStart } from '../../redux/register';
import { CustomInputComponent } from '../common/customInput';

// own
import styles from '../common/styles.module.css';

const { formWrap, title, submit, backgroundSection, systemError, fpPhoneWrapper, fpPhone, fpInput } = styles;

const LoginSchema = Yup.object().shape({
    email: Yup.string()
        .email('Invalid email')
        .required('Required'),
    phone: Yup.string()
        .required('Required'),
});

class ForgotPasswordForm extends Component {
    render() {
        const { resetPasswordStart, resetPasswordValidateStart, error, isLoading, isResetValidate } = this.props;
        const handleSubmit = (e) => {
            if(isResetValidate) {
                return resetPasswordValidateStart(e);
            }
            resetPasswordStart(e);
        }
        const errorMsg = _.get(error, 'errorMsg');
        return (
            <div className={ formWrap }>
                <h1 className={ title }>Увійти</h1>
                { errorMsg&& (
                    <div className={ systemError }>
                        {errorMsg}
                    </div>
                ) }
                <Formik
                    initialValues={ { email: '', phone: '', 'code': '' } }
                    validationSchema={ LoginSchema }
                    onSubmit={ handleSubmit }
                >
                    { () => (
                        <Form>
                            <div className={ backgroundSection }>
                                <Field
                                    name='email'
                                    placeholder='Електронна пошта'
                                    type='email'
                                    errors={ errorMsg }
                                    component={ CustomInputComponent }
                                    disabled={ isResetValidate }
                                />
                                <div className={ fpPhoneWrapper }>
                                    <span className={ fpPhone }>+38</span>
                                    <div className={ fpInput }>
                                        <Field
                                            name='phone'
                                            placeholder='Телефон'
                                            type='phone'
                                            errors={ errorMsg }
                                            component={ CustomInputComponent }
                                            disabled={ isResetValidate }
                                        />
                                    </div>
                                </div>
                                { isResetValidate && (
                                    <Field
                                        name='code'
                                        placeholder='SMS-код'
                                        component={ CustomInputComponent }
                                    />
                                ) }
                                <button
                                    disabled={ isLoading }
                                    className={ submit }
                                    type='submit'
                                >
                                    { isResetValidate ? 'Надіслати пароль' : 'Продовжити' }
                                </button>
                            </div>
                        </Form>
                    ) }
                </Formik>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    error:           state.register.registerErrors,
    isLoading:       state.register.isLoading,
    isResetValidate: state.register.isResetValidate,
});

const mapDispatchToProps = { resetPasswordStart, resetPasswordValidateStart };

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(ForgotPasswordForm);
