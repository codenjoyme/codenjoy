// core
import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import { connect } from 'react-redux';
import * as Yup from 'yup';

// proj
import { login } from 'redux/auth';

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
        const { login } = this.props;

        return (
            <div>
                <h1>Логін</h1>
                <Formik initialValues={ { email: '', password: '' } } validationSchema={ LoginSchema } onSubmit={ login }>
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
    failed: state.auth.failed,
});

export default connect(
    mapStateToProps,
    { login },
)(LoginForm);
