// core
import React, { Component } from 'react';
import { Formik, Form, Field } from 'formik';
import { NavLink } from 'react-router-dom';
import { connect } from 'react-redux';
import * as Yup from 'yup';
import _ from 'lodash';

// proj
import { login } from '../../redux/auth';
import { book } from '../../routes';
import { CustomInputComponent } from '../common/customInput';
import errorImg from '../common/Bomb_server_Error.jpg';

// own
import styles from '../common/styles.module.css';

const { formWrap, title, submit, backgroundSection, systemError, checkBoxLabel, forgotPasswordLink } = styles;

const LoginSchema = Yup.object().shape({
    email: Yup.string()
        .email('Invalid email')
        .required('Required'),
    password: Yup.string()
        .min(2, 'Too Short!')
        .max(50, 'Too Long!')
        .required('Required'),
});

class LoginForm extends Component {
    render() {
        const { login, loginErrors, isLoading } = this.props;
        const error = _.get(loginErrors, 'errorMsg');
        return (
            <div className={ formWrap }>
                <h1 className={ title }>Увійти</h1>
                { error && (
                    <div className={ systemError }>
                        <img src={ errorImg } alt='' />
                        { error }
                    </div>
                ) }
                <Formik
                    initialValues={ { email: '', password: '' } }
                    validationSchema={ LoginSchema }
                    onSubmit={ login }
                >
                    { () => (
                        <Form>
                            <div className={ backgroundSection }>
                                <Field
                                    name='email'
                                    placeholder='Електронна пошта'
                                    type='email'
                                    errors={ error }
                                    component={ CustomInputComponent }
                                />
                                <Field
                                    name='password'
                                    placeholder='Пароль'
                                    type='password'
                                    errors={ error }
                                    component={ CustomInputComponent }
                                />
                                <button
                                    disabled={ isLoading }
                                    className={ submit }
                                    type='submit'
                                >
                                    Увійти
                                </button>
                            </div>
                        </Form>
                    ) }
                </Formik>
                <NavLink to={ book.forgotPassword }>
                    <div className={ `${checkBoxLabel} ${forgotPasswordLink}` }><span>Забув пароль?</span></div>
                </NavLink>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    loginErrors: state.auth.loginErrors,
    isLoading:   state.auth.isLoading,
});

const mapDispatchToProps = { login };

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(LoginForm);
