export function getError(err) {
  if (!err) { return 'Щось пішло не так'; }
  if (typeof err === 'string') {
    return err;
  } else if (err && err.responseText) {
    return err.responseText;
  } else if (err && err.statusText) {
    return err.statusText;
  } else if (err.message) {
    return err.message;
  }

  return 'Щось пішло не так';
}
