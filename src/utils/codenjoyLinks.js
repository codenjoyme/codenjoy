export const getGameConnectionString = (server, code, email) =>
    `http://${server}/codenjoy-contest/board/player/${email}?code=${code}`;

export const getIFrameLink = (server, id) =>
    `http://${server}/codenjoy-contest/board/player/id/${id}?only=true`;
