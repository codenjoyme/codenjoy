// core
import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import { connect } from 'react-redux';
import * as Yup from 'yup';
import _ from 'lodash';

// proj
import { register } from '../../redux/register';

const requiredShortString = Yup.string()
    .min(2, 'Too Short!')
    .max(50, 'Too Long!')
    .required('Required');

const requiredString = Yup.string()
    .min(2, 'Too Short!')
    .max(255, 'Too Long!')
    .required('Required');

const RegisterSchema = Yup.object().shape({
    password:  requiredShortString,
    firstName: requiredShortString,
    lastName:  requiredShortString,
    city:      requiredShortString,
    skills:    requiredString,

    email: Yup.string()
        .email('Invalid email')
        .required('Required'),
    passwordConfirm: Yup.string()
        .oneOf([ Yup.ref('password'), null ], 'Passwords should be equal')
        .required('Password confirm is required'),
});

class LoginForm extends Component {
    render() {
        const { register, registerErrors } = this.props;

        return (
            <div>
                <h1>Реєстрація</h1>
                { _.get(registerErrors, 'credentials') && (
                    <div>Такий користувач уже існує</div>
                ) }
                { _.get(registerErrors, 'system') && (
                    <div>Сервіс тимчасово недоступний</div>
                ) }
                <Formik
                    initialValues={ {
                        password:        '',
                        lastName:        '',
                        firstName:       '',
                        passwordConfirm: '',
                        city:            '',
                        email:           '',
                        skills:          '',
                    } }
                    validationSchema={ RegisterSchema }
                    onSubmit={ payload =>
                        register(_.omit(payload, [ 'passwordConfirm' ]))
                    }
                >
                    { () => (
                        <Form>
                            Email:
                            <ErrorMessage name='email' component='div' />
                            <Field type='email' name='email' />
                            <br />
                            Password:
                            <ErrorMessage name='password' component='div' />
                            <Field type='password' name='password' />
                            <br />
                            passwordConfirm:
                            <ErrorMessage
                                name='passwordConfirm'
                                component='div'
                            />
                            <Field type='password' name='passwordConfirm' />
                            <br />
                            firstName:
                            <ErrorMessage name='firstName' component='div' />
                            <Field type='text' name='firstName' />
                            <br />
                            lastName:
                            <ErrorMessage name='lastName' component='div' />
                            <Field type='text' name='lastName' />
                            <br />
                            city:
                            <ErrorMessage name='city' component='div' />
                            <Field type='text' name='city' />
                            <br />
                            skills:
                            <ErrorMessage name='skills' component='div' />
                            <Field type='text' name='skills' />
                            <br />
                            <button type='submit'>Зареєструватися</button>
                        </Form>
                    ) }
                </Formik>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    registerErrors: state.register.registerErrors,
});

const mapDispatchToProps = { register };

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(LoginForm);
