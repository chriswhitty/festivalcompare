const gulp = require('gulp');
const webpack = require('webpack-stream');
const mocha = require('gulp-mocha');

const webpackConfig = require('./webpack.config.js');

gulp.task('test', function() {
    return gulp.src('src/test/spec', {read: false})
        .pipe(mocha({compilers: "js:babel-core/register", reporter: 'nyan'}))
});

gulp.task('run_e2e', function() {
    return gulp.src('src/test/integration', {read: false})
        .pipe(mocha({compilers: "js:babel-core/register"}))
});

gulp.task('webpack', ['test'], function() {
    return gulp.src('src/main/js/main.js')
        .pipe(webpack(webpackConfig))
        .pipe(gulp.dest('build/gulp/'));
});

gulp.task('static', function() {
    return gulp.src('src/main/resources/*')
        .pipe(gulp.dest('build/gulp/'));
});


gulp.task('build', ['webpack', 'static']);
gulp.task('default', ['build']);
