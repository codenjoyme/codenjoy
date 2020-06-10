export function getError(err) {
  return new Promise(resolve => {
    if (!err) { return resolve('Щось пішло не так'); }
    if (typeof err === 'string') {
      return resolve(err);
    } else if (err.text) {
      return err.text().then(parseBackendError).then(resolve);
    } else if (err && err.responseText) {
      return resolve(err.responseText);
    } else if (err && err.statusText) {
      return resolve(err.statusText);
    } else if (err.message) {
      return resolve(err.message);
    }

    return resolve('Щось пішло не так');
  })
}

export function getErrorObject(err) {
  return getError(err).then((errorMsg) => {
    return {
      errorMsg,
    }
  })
}

export function parseBackendError(err) {
  if(err.message || err.ticketNumber) {
    return `${err.message || ''} ${err.ticketNumber || ''}`;
  }

  return err;
}
