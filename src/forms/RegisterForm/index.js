// core
import React, { Component } from 'react';
import { Formik, Form, Field } from 'formik';
import { connect } from 'react-redux';
import * as Yup from 'yup';
import _ from 'lodash';
import styles from '../common/styles.module.css';
import { CustomInputComponent } from '../common/customInput';
import { CustomCheckboxComponent } from '../common/customCheckbox';
import { CustomSelectComponent } from '../common/customSelect';

// proj
import { register } from '../../redux/register';

const { formWrap, title, submit, backgroundSection } = styles;

const requiredShortString = Yup.string()
    .min(2, 'Too Short!')
    .max(50, 'Too Long!')
    .required('Required');
const optionalString = Yup.string().nullable(true);

const RegisterSchema = Yup.object().shape({
    password:  requiredShortString,
    firstName: requiredShortString,
    lastName:  requiredShortString,
    city:      requiredShortString,
    skills:    requiredShortString,
    others:    Yup.string().when('skills', {
        is:        'other',
        then:      requiredShortString,
        otherwise: optionalString,
    }),
    terms: Yup.boolean().oneOf([ true ]),

    email: Yup.string()
        .email('Invalid email')
        .required('Required'),
    passwordConfirm: Yup.string()
        .oneOf([ Yup.ref('password'), null ], 'Passwords should be equal')
        .required('Password confirm is required'),
});

const OTHER_VALUE = 'other';
const options = [
    {
        label: 'Java',
        value: 'java',
    },
    {
        label: 'JavaScript',
        value: 'javaScript',
    },
    {
        label: 'BigData',
        value: 'bigData',
    },
    {
        label: 'Automated Testing',
        value: 'dutomatedTesting',
    },
    {
        label: 'Functional Testing',
        value: 'dunctionaTesting',
    },
    {
        label: 'DevOps.CI/CD',
        value: 'devOps.CI/CD',
    },
    {
        label: 'Project Management',
        value: 'projectManagement',
    },
    {
        label: 'Android',
        value: 'android',
    },
    {
        label: 'Other',
        value: OTHER_VALUE,
    },
];

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
                        others:          '',
                        terms:           false,
                    } }
                    validationSchema={ RegisterSchema }
                    onSubmit={ payload => {
                        const { skills, others, ...otherFields } = payload;
                        const user = {
                            ..._.omit(otherFields, 'passwordConfirm'),
                            skills: skills === OTHER_VALUE ? others : skills,
                        };

                        register(user);
                    } }
                >
                    { props => (
                        <Form>
                            <div className={ backgroundSection }>
                                <Field
                                    name='firstName'
                                    placeholder='Ім`я*'
                                    component={ CustomInputComponent }
                                />
                                <Field
                                    name='lastName'
                                    placeholder='Прізвище*'
                                    component={ CustomInputComponent }
                                />
                                <Field
                                    type='email'
                                    name='email'
                                    placeholder='Електронна пошта*'
                                    component={ CustomInputComponent }
                                />
                                <Field
                                    type='password'
                                    name='password'
                                    placeholder='Пароль*'
                                    component={ CustomInputComponent }
                                />
                                <Field
                                    type='password'
                                    name='passwordConfirm'
                                    placeholder='Повторіть Пароль*'
                                    component={ CustomInputComponent }
                                />
                                <Field
                                    name='city'
                                    placeholder='Місто*'
                                    component={ CustomInputComponent }
                                />
                                <Field
                                    component={ CustomSelectComponent }
                                    placeholder='Спеціалізація*'
                                    name='skills'
                                    options={ options }
                                />
                                { _.get(props, 'values.skills') ===
                                    OTHER_VALUE && (
                                    <Field
                                        name='others'
                                        placeholder='Інше*'
                                        component={ CustomInputComponent }
                                    />
                                ) }
                            </div>

                            <Field
                                name='terms'
                                component={ CustomCheckboxComponent }
                                label='Погоджуюсь с політикою конфіденційності*'
                                type='checkbox'
                            />

                            <div className={ backgroundSection }>
                                <button className={ submit } type='submit'>
                                    Зареєструватися
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
    registerErrors: state.register.registerErrors,
});

const mapDispatchToProps = { register };

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(LoginForm);
