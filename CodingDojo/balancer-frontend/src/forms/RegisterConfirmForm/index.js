// core
import React, { Component } from 'react';
import { Formik, Form, Field } from 'formik';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { withRouter } from 'react-router-dom';
// import * as Yup from 'yup';
import _ from 'lodash';

// proj
import { registerConfirmStart, registerResendStart } from '../../redux/register';
import { book } from '../../routes';
import { CustomInputComponent } from '../common/customInput';
import errorImg from '../common/Bomb_server_Error.jpg';
// own
import styles from '../common/styles.module.css';

const { formWrap, title, submit, backgroundSection, checkBoxLabel, systemError, registerConfirmResend } = styles;

class RegisterConfirmForm extends Component {
    componentDidMount() {
        const { shouldConfirmRegistration, history } = this.props;
        if(!shouldConfirmRegistration) {
            history.push(`${book.register}`)
        }

    }
    handleResend = () => {
        this.props.registerResendStart();
    }
    render() {
        const { registerConfirmStart, registerErrors, isLoading } = this.props;
        const errorMsg = _.get(registerErrors, 'errorMsg');
        return (
            <div className={ formWrap }>
                <h1 className={ title }>Новий Гравець</h1>
                { errorMsg && (
                    <div className={ systemError }>
                        <img src={ errorImg } alt='' />
                        {errorMsg}
                    </div>
                ) }
                <Formik
                    initialValues={ { code: '' } }
                    onSubmit={ registerConfirmStart }
                >
                    { () => (
                        <Form>
                            <div className={ backgroundSection }>
                                <Field
                                    name='code'
                                    placeholder='SMS-код'
                                    component={ CustomInputComponent }
                                    errors={ _.get(
                                        registerErrors,
                                        'errorMsg',
                                    ) }
                                />
                            </div>
                            <div className={ `${checkBoxLabel} ${registerConfirmResend}` }><span onClick={ this.handleResend }>Надіслати ще раз</span></div>
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
    registerErrors:            state.register.registerErrors,
    isLoading:                 state.register.isLoading,
    shouldConfirmRegistration: state.register.shouldConfirmRegistration,
});

const mapDispatchToProps = { registerConfirmStart, registerResendStart };

export default compose(
    connect(
        mapStateToProps,
        mapDispatchToProps,
    ),
    withRouter,
)(RegisterConfirmForm);
