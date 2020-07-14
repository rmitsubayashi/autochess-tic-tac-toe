package com.github.rmitsubayashi.ui.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.rmitsubayashi.GdxGame
import com.github.rmitsubayashi.action.Action

open class IStageScreen(private val game: GdxGame): Screen {
    var stage: Stage
    // need to keep track of all actions
    // so we can unsubscribe them when the screen is disposed
    private val actions = mutableListOf<Action>()

    init {
        val width = 480f
        val height = 800f
        // should I have a static SpriteBatch instance to pass here?
        // what are the merits??
        stage = Stage(ExtendViewport(width, height))
    }

    override fun hide() {

    }

    override fun show() {

    }

    override fun render(delta: Float) {
        // clears the screen first
        Gdx.gl.glClearColor( 1f, 0f, 0f, 1f )
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act()
        stage.draw()
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        game.game.actionObservable.unsubscribeActions(actions)
    }

    protected fun subscribeAction(action: Action) {
        actions.add(action)
        game.game.actionObservable.subscribeAction(action)
    }
}