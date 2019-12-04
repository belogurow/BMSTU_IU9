# Lab1 - Image process

* Rotate over center
* Shrink
* Translate
* Flip


#### Example of usage

Input

![](silniy_kot.jpg)


```python
    img = cv2.imread('silniy_kot.jpg')

    new_image = ImageProcess(img) \
        .translate(x=10, y=30) \
        .rotate(degrees=45) \
        .flip(over_y=True) \
        .shrink(factor=0.8) \
        .save()
```

Ouput

![](result.jpg)
