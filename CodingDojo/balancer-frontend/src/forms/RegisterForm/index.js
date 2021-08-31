// core
import React, { Component } from 'react';
import { Formik, Form, Field } from 'formik';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { withRouter } from 'react-router-dom';

import * as Yup from 'yup';
import _ from 'lodash';
import { book } from '../../routes';
// proj
import { register, setVisiblePrivacyModal } from '../../redux/register';
import { PrivacyPolicyModal } from '../../components';
import { CustomInputComponent } from '../common/customInput';
import { CustomCheckboxComponent } from '../common/customCheckbox';
import { CustomSelectComponent } from '../common/customSelect';
import errorImg from '../common/Server_Error.jpg';
import { PhoneInput } from 'forms/common/phoneInput';
// own
import styles from '../common/styles.module.css';

const { formWrap, title, submit, backgroundSection, systemError, checkBoxText } = styles;

// errors
const errorNeededValue = "Обов'язкове поле!";
const errorWrongFormat = 'Невірний формат';
const errorToSmall = 'Замало символів!';
const errorToBig = 'Забагато символів!';
const errorEnglishLetters = 'Тільки англійські літери!';
const errorPasswordNotEqual = 'Паролі мають співпадати!';

const requiredShortString = Yup.string()
    .min(2, errorToSmall)
    .max(50, errorToBig)
    .required(errorNeededValue);
const optionalString = Yup.string().nullable(true);

const requiredEnglishShortString = requiredShortString
    .matches(/^[a-zA-Z -]+$/, errorEnglishLetters);

const requiredPhoneString = requiredShortString
    .matches(/^\d{10}$/, errorWrongFormat);


const RegisterSchema = Yup.object().shape({
    password:  requiredShortString,
    phone:     requiredPhoneString,
    updates:   Yup.boolean().oneOf([ true ]),
    firstName: requiredEnglishShortString,
    lastName:  requiredEnglishShortString,
    city:      requiredEnglishShortString,
    skills:    requiredShortString,
    others:    Yup.string().when('skills', {
        is:        'other',
        then:      requiredShortString,
        otherwise: optionalString,
    }),
    terms: Yup.boolean().oneOf([ true ]),

    email: Yup.string()
        .email(errorWrongFormat)
        .required(errorNeededValue),
    passwordConfirm: Yup.string()
        .oneOf([ Yup.ref('password'), null ], errorPasswordNotEqual)
        .required(errorNeededValue),
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
        label: 'Golang',
        value: 'golang',
    },
    {
        label: 'Python',
        value: 'python',
    },
    {
        label: '.NET',
        value: 'dotNet',
    },
    {
        label: 'Python',
        value: 'python',
    },
    {
        label: 'C#',
        value: 'cSharp',
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
    componentDidUpdate() {
        const { history, shouldConfirmRegistration } = this.props;
        if(shouldConfirmRegistration) {
            history.push(`${book.registerConfirm}`)
        }
    }

    render() {
        const { register, setVisiblePrivacyModal } = this.props;
        const { visiblePrivacyModal, registerErrors, isLoading } = this.props;
        const errorMsg = _.get(registerErrors, 'errorMsg')
        return (
            <div className={ formWrap }>
                <h1 className={ title }>Новий гравець</h1>
                { errorMsg&& (
                    <div className={ systemError }>
                        <img src={ errorImg } alt='' />
                        {errorMsg}
                    </div>
                ) }
                <Formik
                    initialValues={ {
                        password:        '',
                        lastName:        '',
                        firstName:       '',
                        passwordConfirm: '',
                        phone:           '',
                        city:            '',
                        email:           '',
                        skills:          '',
                        others:          '',
                        terms:           false,
                        updates:         false,
                    } }
                    validationSchema={ RegisterSchema }
                    onSubmit={ payload => {
                        const { skills, others, ...otherFields } = payload;
                        const user = {
                            ..._.omit(otherFields, [ 'passwordConfirm', 'terms' ]),
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
                                      type='phone'
                                      name='phone'
                                      placeholder='Номер телефону*'
                                      component={ PhoneInput }
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
                                label={
                                    <div
                                        className={ checkBoxText }
                                        onClick={ () =>
                                            setVisiblePrivacyModal(true)
                                        }
                                    >
                                        Погоджуюсь с політикою конфіденційності
                                        <div style={{ textDecoration: 'underline' }}>
                                            (будь ласка, клікніть на посилання для подробиць)
                                        </div>
                                    </div>
                                }
                                type='checkbox'
                            />

                            <Field
                                name='updates'
                                component={ CustomCheckboxComponent }
                                label={
                                    <div
                                        className={ checkBoxText }
                                    >
                                        Я хочу отримувати листи про можливість віддаленої роботи: вакансії, новини статті, заходи та іншу інформацію, пов'язану з інноваційною програмою для IT-спеціалістів та розробників, які хочуть працювати віддалено
                                    </div>
                                }
                                type='checkbox'
                            />
                            <PrivacyPolicyModal
                                action={ accept => {
                                    props.setFieldTouched('terms');
                                    props.setFieldValue('terms', accept);
                                } }
                                isOpen={ visiblePrivacyModal }
                                setVisible={ setVisiblePrivacyModal }
                            />

                            <div className={ backgroundSection }>
                                <button
                                    disabled={ isLoading }
                                    className={ submit }
                                    type='submit'
                                >
                                    Продовжити
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
    registerErrors:            state.register.registerErrors,
    isLoading:                 state.register.isLoading,
    visiblePrivacyModal:       state.register.visiblePrivacyModal,
    shouldConfirmRegistration: state.register.shouldConfirmRegistration,
});

const mapDispatchToProps = { register, setVisiblePrivacyModal };

export default compose(
    connect(
        mapStateToProps,
        mapDispatchToProps,
    ),
    withRouter,
)(LoginForm);
