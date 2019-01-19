export const getGameConnectionString = (server, code, email) =>
    `http://${server}/codenjoy-contest/board/player/${email}?code=${code}`;
