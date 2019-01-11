//
// localStorage
//
// token
export const setToken = token => localStorage.setItem('__codenjoy_token__', token);

export const getToken = () => localStorage.getItem('__codenjoy_token__');

export const removeToken = () => localStorage.removeItem('__codenjoy_token__');

// username
export const setUsername = username => localStorage.setItem('__codenjoy_username__', username);

export const getUsername = () => localStorage.getItem('__codenjoy_username__');

export const removeUsername = () => localStorage.removeItem('__codenjoy_username__');
