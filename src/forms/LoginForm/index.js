// core
import React, { Component } from 'react';
import { Formik, Form, Field } from 'formik';
import { connect } from 'react-redux';
import * as Yup from 'yup';
import _ from 'lodash';

// proj
import { login } from '../../redux/auth';
import { CustomInputComponent } from '../common/customInput';
import errorSnake from '../common/DuneSnake-icon.svg';

// own
import styles from '../common/styles.module.css';

const { formWrap, title, submit, backgroundSection, systemError } = styles;

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

        return (
            <div className={ formWrap }>
                <h1 className={ title }>Увійти</h1>
                { _.get(loginErrors, 'system') && (
                    <div className={ systemError }>
                        <img src={ errorSnake } alt='' />
                        Через непередбачуваний політ діда Мороза антени було
                        пошкоджено. <br />
                        Як тільки пошкодження будуть усунені, сервіс буде
                        доступним.
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
                                    errors={ _.get(loginErrors, 'credentials') }
                                    component={ CustomInputComponent }
                                />
                                <Field
                                    name='password'
                                    placeholder='Пароль'
                                    type='password'
                                    errors={ _.get(loginErrors, 'credentials') }
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
