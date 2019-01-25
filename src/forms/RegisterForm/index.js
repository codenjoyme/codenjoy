// core
import React, { Component } from 'react';
import { Formik, Form, Field } from 'formik';
import { connect } from 'react-redux';
import * as Yup from 'yup';
import _ from 'lodash';

// proj
import { register, setVisiblePrivacyModal } from '../../redux/register';
import { PrivacyPolicyModal } from '../../components';
import { CustomInputComponent } from '../common/customInput';
import { CustomCheckboxComponent } from '../common/customCheckbox';
import { CustomSelectComponent } from '../common/customSelect';
import errorSnake from '../common/DuneSnake-icon.svg';

// own
import styles from '../common/styles.module.css';

const { formWrap, title, submit, backgroundSection, systemError } = styles;

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
        const { register, setVisiblePrivacyModal } = this.props;
        const { visiblePrivacyModal, registerErrors, isLoading } = this.props;

        return (
            <div className={ formWrap }>
                <h1 className={ title }>Новий гравець</h1>
                { _.get(registerErrors, 'system') && (
                    <div className={ systemError }>
                        <img src={ errorSnake } alt='' />
                        Через непередбачуваний політ діда Мороза антени було
                        пошкоджено. <br />
                        Як тільки пошкодження будуть усунені, сервіс буде
                        доступним.
                    </div>
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
                                    errors={ _.get(
                                        registerErrors,
                                        'credentials',
                                    ) }
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
                                label={
                                    <div
                                        onClick={ () =>
                                            setVisiblePrivacyModal(true)
                                        }
                                    >
                                        Погоджуюсь с політикою конфіденційності*
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
    registerErrors:      state.register.registerErrors,
    isLoading:           state.register.isLoading,
    visiblePrivacyModal: state.register.visiblePrivacyModal,
});

const mapDispatchToProps = { register, setVisiblePrivacyModal };

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(LoginForm);
