const {describe, it} = require('mocha');
const webdriver = require('selenium-webdriver');
const {expect} = require('chai');

const By = webdriver.By;
const until = webdriver.until;
By.dataQa = (name) => By.css(`[data-qa='${name}']`);

describe('Creating a new festival', function () {

    let driver;

    before(() => {
        driver = new webdriver.Builder()
            .forBrowser('chrome')
            .build();
        driver.manage().setTimeouts({implicit: 500});
    });

    beforeEach(() => {
        let host = process.env.E2E_HOST ? process.env.E2E_HOST : 'http://localhost:8080';
        return driver.navigate().to(host);
    });

    after(() => {
        return driver.quit();
    });


    it('should show Spotify scores when searching for artists', function () {

        driver.findElement(By.dataQa('artist-input')).sendKeys("The National");
        driver.findElement(By.dataQa('submit')).click();

        driver.wait(until.elementLocated(By.dataQa('festival-score')), 4000);
        return driver.findElement(By.dataQa('festival-score')).getText().then((element) => {
            expect(element).to.equal("74");
        });
    });


});