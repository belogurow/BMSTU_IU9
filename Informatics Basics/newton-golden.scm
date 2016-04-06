(define (newton f ff x e)
  (let ((calc (- x (/ (f x) (ff x)))))
    (if (< (f calc) e)
        calc
        (newton f ff calc e))))

(define (golden f a b e)
  (let* ((phi (/ (+ 1 (sqrt 5)) 2))
         (x1 (- b (/ (- b a) phi)))
         (x2 (+ a (/ (- b a) phi))))
    (if (< (abs (- b a)) e)
        (/ (+ a b) 2)
        (if (>= (f x1) (f x2))
            (golden f x1 b e)
            (golden f a x2 e)))))
  