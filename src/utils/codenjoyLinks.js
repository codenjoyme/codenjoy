export const getGameConnectionString = (server, code, email) =>
    `ws://${server}/codenjoy-contest/ws?user=${email}&code=${code}`;
