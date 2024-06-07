package com.muen.gamelink.game.entity

class LinkInfo {
    //创建一个List用于存放两个AnimalView之间的连接信息
    private var points: ArrayList<AnimalPoint> = arrayListOf()

    constructor()

    //存储无转折的点信息
    constructor(pointA: AnimalPoint, pointB: AnimalPoint) {
        points.add(pointA)
        points.add(pointB)
    }

    //存储有一个转折的点信息
    constructor(pointA: AnimalPoint, pointC: AnimalPoint, pointB: AnimalPoint) {
        points.add(pointA)
        points.add(pointC)
        points.add(pointB)
    }

    //存储有两个转折的点信息
    constructor(
        pointA: AnimalPoint,
        pointC: AnimalPoint,
        pointD: AnimalPoint,
        pointB: AnimalPoint
    ) {
        points.add(pointA)
        points.add(pointC)
        points.add(pointD)
        points.add(pointB)
    }

    //setter、getter方法
    fun getPoints(): ArrayList<AnimalPoint> {
        return points
    }

    fun setPoints(points: ArrayList<AnimalPoint>) {
        this.points = points
    }
}