const protocol = process.env.REACT_APP_IS_SECURE ? 'https' : 'http';

export const getGameConnectionString = (server, code, email) =>
    `${protocol}://${server}/codenjoy-contest/board/player/${email}?code=${code}`;

export const getIFrameLink = (server, id) =>
    `${protocol}://${server}/codenjoy-contest/board/player/id/${id}?only=true`;

export const getJsClient = server =>
    `${protocol}://${server}/codenjoy-contest/resources/user/snakebattle-servers-js.zip`;

export const getJavaClient = server =>
    `${protocol}://${server}/codenjoy-contest/resources/user/snakebattle-servers-java.zip`;
