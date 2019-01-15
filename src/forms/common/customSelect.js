import React, { Component } from 'react';
import Select from 'react-select';
import classNames from 'classnames';
import styles from './styles.module.css';

const { selectWrap, select, selectValid, selectError  } = styles;

const customSelectStyles = {
    input: (provided) => ({
        ...provided,
    }),
    container: (provided) => ({
        ...provided,
    }),
    control: (provided) => ({
        ...provided,
        borderRadius: 0,
        background: '#263852',
    }),
    menu: (provided) => ({
        ...provided,
        borderRadius: 0,
        background: '#263852',
    }),
    option: (provided) => ({
        ...provided,
        borderRadius: 0,
    }),
}

export class CustomSelectComponent extends Component {
    handleChange = value => {
        // this is going to call setFieldValue and manually update values.topcis
        // this.props.onChange('topics', value);
    };

    handleBlur = () => {
        // this is going to call setFieldTouched and manually update touched.topcis
        // this.props.onBlur('topics', true);
    };

    render() {
        const { field, form: { touched, errors } } = this.props;
        const isCurrentFieldNotValid = touched[ field.name ] && errors[ field.name ];
        const isCurrentFieldValid = touched[ field.name ] && !errors[ field.name ];

        return (
            <div className={ selectWrap }>
                <Select
                    className={ classNames(select, { [ selectError ]: isCurrentFieldNotValid, [ selectValid ]: isCurrentFieldValid  }) }
                    styles={ customSelectStyles }
                    options={ this.props.children }
                    multi={ false }
                    onChange={ this.handleChange }
                    onBlur={ this.handleBlur }
                    value={ this.props.value }
                />
                { !!this.props.error &&
                this.props.touched && (
                    <div style={ { color: 'red', marginTop: '.5rem' } }>{ this.props.error }</div>
                ) }
            </div>
        );
    }
}
