package com.example.movecaronmap.model

interface ICarActions {
    fun move()
    fun turn(turningRadius: Float)
    fun stop()
}