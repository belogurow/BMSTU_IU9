(define pi (acos -1))

(define (number-of-pots d h sq)
  (let* ((s (* 2 pi (/ d 2) (+ (/ d 2) (* 2 h)))))
    (if (< (round (/ s sq)) (/ s sq)) 
        (+ (round (/ s sq)) 1)
        (round (/ s sq)))))