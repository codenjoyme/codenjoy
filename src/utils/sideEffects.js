//
// localStorage
//
// code
export const setCode = code => localStorage.setItem('__codenjoy_code__', code);

export const getCode = () => localStorage.getItem('__codenjoy_code__');

export const removeCode = () => localStorage.removeItem('__codenjoy_code__');

// email
export const setEmail = email =>
    localStorage.setItem('__codenjoy_email__', email);

export const getEmail = () => localStorage.getItem('__codenjoy_email__');

export const removeEmail = () => localStorage.removeItem('__codenjoy_email__');

// server
export const setServer = server =>
    localStorage.setItem('__codenjoy_server__', server);

export const getServer = () => localStorage.getItem('__codenjoy_server__');

export const removeServer = () =>
    localStorage.removeItem('__codenjoy_server__');

// id
export const setId = id => localStorage.setItem('__codenjoy_id__', id);

export const getId = () => localStorage.getItem('__codenjoy_id__');

export const removeId = () => localStorage.removeItem('__codenjoy_id__');
