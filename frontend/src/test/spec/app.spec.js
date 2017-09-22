import App from '../../main/js/App';

const {describe, it} = require('mocha');
const {expect} = require('chai');
const {JSDOM} = require('jsdom');

const React = require('react');
const { mount, shallow }  =  require('enzyme');

describe('App', function () {


    const jsdom = new JSDOM('<!doctype html><html><body></body></html>');
    const { window } = jsdom;

    global.window = window;
    global.document = window.document;
    global.navigator = {
        userAgent: 'node.js',
    };


    beforeEach(() => {
    });

    after(() => {
    });


    it('should render a header', function () {

        const wrapper = mount(<App />);
        expect(wrapper.contains(<h1>Festival Compare</h1>)).to.equal(true);
    });


});