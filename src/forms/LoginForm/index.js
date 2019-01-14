// core
import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import { connect } from 'react-redux';
import * as Yup from 'yup';
import _ from 'lodash';

// proj
import { login } from '../../redux/auth';

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
        const { login, loginErrors } = this.props;

        return (
            <div>
                <h1>Логін</h1>
                { _.get(loginErrors, 'credentials') && (
                    <div>Неправильний емейл або пароль</div>
                ) }
                { _.get(loginErrors, 'system') && (
                    <div>Сервіс тимчасово недоступний</div>
                ) }
                <Formik
                    initialValues={ { email: '', password: '' } }
                    validationSchema={ LoginSchema }
                    onSubmit={ login }
                >
                    { () => (
                        <Form>
                            <ErrorMessage name='email' component='div' />
                            <Field type='email' name='email' />
                            <ErrorMessage name='password' component='div' />
                            <Field type='password' name='password' />
                            <button type='submit'>Submit</button>
                        </Form>
                    ) }
                </Formik>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    loginErrors: state.auth.loginErrors,
});

const mapDispatchToProps = { login };

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(LoginForm);
