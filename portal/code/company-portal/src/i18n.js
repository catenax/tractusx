import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

import en from "./locales/en/translations.json"
import de from "./locales/de/translations.json"

const resources = {
    en,
    de
}

i18n
    .use(initReactI18next)
    .init({
        resources,
        lng: "en",
        interpolation: {
            escapeValue: false
        }
    });


export default i18n;