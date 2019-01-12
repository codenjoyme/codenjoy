export const getGameConnectionString = (server, code, email) =>
    `${server}/codenjoy-contest/board/player/${email}?code=${code}`;
