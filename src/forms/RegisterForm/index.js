// core
import React, { Component } from 'react';
import { Formik, Form, Field } from 'formik';
import { connect } from 'react-redux';
import * as Yup from 'yup';
import _ from 'lodash';
import styles from '../common/styles.module.css';
import { CustomInputComponent } from '../common/customInput';
import { CustomSelectComponent } from '../common/customSelect';

// proj
import { register } from '../../redux/register';

const { formWrap, title, form, submit } = styles;

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
            <div className={ formWrap }>
                <h1 className={ title }>Новий гравець</h1>
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
                        color:           '',
                    } }
                    validationSchema={ RegisterSchema }
                    onSubmit={ payload =>
                        register(_.omit(payload, [ 'passwordConfirm' ]))
                    }
                >
                    { () => (
                        <Form className={ form }>
                            <Field name='firstName' placeholder='Ім`я*' component={ CustomInputComponent }/>
                            <Field name='lastName' placeholder='Призвіще*' component={ CustomInputComponent }/>
                            <Field type='email' name='email' placeholder='Електронна пошта*' component={ CustomInputComponent }/>
                            <Field type='password' name='password' placeholder='Пароль*' component={ CustomInputComponent }/>
                            <Field type='password' name='passwordConfirm' placeholder='Повтроріть Пароль*' component={ CustomInputComponent }/>
                            <Field name='city' placeholder='Місто*' component={ CustomInputComponent }/>
                            <Field name='skills' placeholder='Спеціалізація*' component={ CustomInputComponent }/>
                            <Field component={ CustomSelectComponent } name='color'>
                                <option value='red'>Red</option>
                                <option value='green'>Green</option>
                                <option value='blue'>Blue</option>
                            </Field>
                            <Field component='input' type='checkbox' />

                            <button className={ submit } type='submit'>Зареєструватися</button>
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
