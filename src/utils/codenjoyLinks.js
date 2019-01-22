export const getGameConnectionString = (server, code, email) =>
    `http://${server}/codenjoy-contest/board/player/${email}?code=${code}`;

export const getIFrameLink = (server, id) =>
    `http://${server}/codenjoy-contest/board/player/id/${id}?only=true`;

export const getJsClient = server =>
    `http://${server}/codenjoy-contest/resources/user/snakebattle-servers-js.zip`;

export const getJavaClient = server =>
    `http://${server}/codenjoy-contest/resources/user/snakebattle-servers-java.zip`;
